package at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model.LHamster;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.InstructionProcessor;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.StepRequest;

/**
 * TODO: Testen der Debuggersteuerung (vor allem ThreadResumed und
 * ConnectionClosed)
 *
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class DebuggerModel extends Observable implements Runnable {
	/**
	 * This argument informs off a state-change.
	 */
	public static final String ARG_ENABLE = "enable";

	/**
	 * This argument informs off a state-change.
	 */
	public static final String ARG_STATE = "state-change";

	/**
	 * In this state the debugger is not running. It has no associated Runner.
	 */
	public static final int NOT_RUNNING = 0;

	/**
	 * In this state the debugger is executing the user-sourcecode.
	 */
	public static final int RUNNING = 2;

	/**
	 *
	 */
	public static final int PAUSED = 5;

	/**
	 * This is the current state of the Debugger.
	 */
	private int state;

	/**
	 * This is the HamsterFile to be debugged.
	 */
	private HamsterFile currentFile;

	/**
	 * The corresponding class name.
	 */
	private String className;

	private boolean enabled;

	private ArrayList lockedFiles;
	protected ArrayList lockedRefs;

	protected int delay;

	protected boolean suspended;

	RemoteRunner runner;

	InstructionProcessor processor;
	VirtualMachine machine;
	EventRequestManager eventRequestManager;
	EventQueue eventQueue;
	StepRequest stepRequest;
	ThreadReference suspendedThread;
	Thread thread;

	// Martin
	Thread hamsterThread;

	protected List stackFrames;

	private LocalProcessor localProcessor;

	public DebuggerModel(InstructionProcessor processor) {
		this.processor = processor;
		this.localProcessor = new LocalProcessor(processor, this);
		this.state = DebuggerModel.NOT_RUNNING;
		this.delay = 0;
		this.lockedFiles = new ArrayList();
		this.lockedRefs = new ArrayList();
	}

	public int getState() {
		return this.state;
	}

	public List getStackFrames() {
		return this.stackFrames;
	}

	public boolean isSuspended() {
		return this.suspended;
	}

	public void start(HamsterFile file) {
		if (this.state != DebuggerModel.NOT_RUNNING) {
			// TODO: ueber Exceptions
			System.out.println("not good");
			return;
		}
		this.currentFile = file;
		if (this.currentFile == null) {
			return;
		}

		if (Utils.runlocally) {
			IHamster.processor = this.localProcessor;
			/* lego */
            LHamster.processor = this.localProcessor;
			Territorium.processor = this.localProcessor;
			Hamster.count = 0;
			this.localProcessor.start();
			this.setState(DebuggerModel.PAUSED);
			try {
				this.localProcessor.run(new HamsterClassLoader().getHamsterInstance(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.className = this.currentFile.getName();
			LaunchingConnector connector = Bootstrap.virtualMachineManager().defaultConnector();
			Map args = connector.defaultArguments();
			Connector.Argument main = (Connector.Argument) args.get("main");
			main.setValue(RemoteRunner.class.getName() + " " + this.className);
			Connector.Argument options = (Connector.Argument) args.get("options");
			options.setValue("-classpath \"" + file.getDir() + Utils.PSEP + System.getProperty("java.class.path")
					+ Utils.PSEP + Workbench.getWorkbench().getProperty("classpath", "") + "\"");
			try {
				this.machine = connector.launch(args);
				this.machine.resume(); // dibo 27082015
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			this.runner = new RemoteRunner(this.processor, this.machine.process());

			this.setState(DebuggerModel.PAUSED);
			this.stepRequest = null;
			this.eventRequestManager = this.machine.eventRequestManager();
			this.eventQueue = this.machine.eventQueue();
			this.runner.start();
		}
	}

	public static boolean isStop = false;

	public void resume() {
		if (this.state == DebuggerModel.PAUSED) {

			if (Utils.runlocally) {
				this.localProcessor.resume();
			} else {
				this.stepInto(this.currentFile);
			}

			this.setState(DebuggerModel.RUNNING);
		} else {
			System.err.println("Error: resume, when not in SUSPENDED state.");
		}
	}

	public void stepOver(HamsterFile file) {
		this.currentFile = file;
		if (this.currentFile != null && (this.currentFile.getType() == HamsterFile.FSM
				|| this.currentFile.getType() == HamsterFile.FLOWCHART)) {
			// gibts nicht
		} else {
			this.step(StepRequest.STEP_OVER);
		}
	}

	public void stepInto(HamsterFile file) {
		this.currentFile = file;

		this.step(StepRequest.STEP_INTO);

	}

	public void pause() {
		if (this.currentFile != null
				&& (this.currentFile.getType() == HamsterFile.SCRATCHPROGRAM
						|| this.currentFile.getType() == HamsterFile.FSM)
				|| this.currentFile.getType() == HamsterFile.FLOWCHART) {
			if (this.state == DebuggerModel.NOT_RUNNING) {
				// TODO: Exceptions
				return;
			}
		}
		this.setState(DebuggerModel.PAUSED);

		// Scratch

		if (Utils.runlocally) {
			this.localProcessor.pause();
		} else {
			if (!this.enabled) {
				this.machine.suspend();
			}
		}

	}

	@Override
	public void run() {
		try {
			for (;;) {
				if (this.removeEvent()) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		this.removeTypeLocks();
		this.suspended = false;
		this.suspendedThread = null;
		this.stackFrames = null;
		this.setState(DebuggerModel.NOT_RUNNING);
		this.machine = null;
	}

	public HamsterFile getProgram(ReferenceType ref) {
		String sourcePath = null;
		try {
			sourcePath = ref.sourcePaths(null).get(0);
		} catch (AbsentInformationException e) {
			return null;
		}
		sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf('.'));

		sourcePath += ".ham";
		String classpath = this.currentFile.getDir() + Utils.PSEP
				+ Workbench.getWorkbench().getProperty("classpath", "");
		String[] cpEntries = classpath.split(Utils.PSEP);
		for (int i = 0; i < cpEntries.length; i++) {
			File f = new File(cpEntries[i]);
			if (!f.isDirectory()) {
				continue;
			}
			File file = new File(f, sourcePath);
			if (file.exists()) {
				return HamsterFile.getHamsterFile(file.getAbsolutePath());
			}
		}
		return null;
	}

	private void removeTypeLocks() {
		for (int i = 0; i < this.lockedFiles.size(); i++) {
			HamsterFile file = (HamsterFile) this.lockedFiles.get(i);
			file.setLocked(false);
		}
		this.lockedFiles.removeAll(this.lockedFiles);
		this.lockedRefs.removeAll(this.lockedRefs);
	}

	private void lockType(ReferenceType ref) {
		HamsterFile file = this.getProgram(ref);
		if (file != null) {
			file.setLocked(true);
			this.lockedFiles.add(file);
		} else {
			if (ref.name().startsWith("java.") || ref.name().startsWith("javax.")
					|| ref.name().startsWith(".hamsterEvaluation.") || ref.name().startsWith("sun.")) {
				return;
			}
			this.lockedRefs.add(ref);
		}
	}

	private boolean removeEvent() throws InterruptedException, IncompatibleThreadStateException {
		EventSet set = this.eventQueue.remove();
		EventIterator eventIterator = set.eventIterator();
		boolean resume = false;
		while (eventIterator.hasNext()) {
			Event event = eventIterator.nextEvent();
			if (event instanceof VMStartEvent) {
				ClassPrepareRequest r = this.eventRequestManager.createClassPrepareRequest();
				r.enable();
				resume = true;
			} else if (event instanceof VMDisconnectEvent) {
				return true;
			} else if (event instanceof VMDeathEvent) {
				resume = true;
			} else if (event instanceof ClassPrepareEvent) {
				ClassPrepareEvent e = (ClassPrepareEvent) event;
				ReferenceType ref = e.referenceType();
				this.lockType(ref);
				if (ref.name().equals(this.className)) {
					MethodEntryRequest entryRequest = this.eventRequestManager.createMethodEntryRequest();
					entryRequest.addClassFilter(this.className);
					entryRequest.enable();
				}
				resume = true;
			} else if (event instanceof MethodEntryEvent) {
				MethodEntryEvent e = (MethodEntryEvent) event;
				Method m = e.method();
				if (m.isConstructor()) {
					resume = true;
				} else {
					this.eventRequestManager.deleteEventRequest(event.request());
					this.suspendedThread = e.thread();
					this.stackFrames = e.thread().frames(0, e.thread().frameCount() - 1);
				}
			} else if (event instanceof StepEvent) {
				StepEvent e = (StepEvent) event;
				this.suspendedThread = e.thread();
				this.stackFrames = e.thread().frames(0, e.thread().frameCount() - 1);
			} else {
				resume = true;
			}
		}
		if (resume | !this.enabled) {
			set.resume();
		} else if (this.state == DebuggerModel.RUNNING) {
			this.setChanged();
			this.suspended = true;
			this.notifyObservers(DebuggerModel.ARG_STATE);
			if (this.state == DebuggerModel.RUNNING) {
				this.suspended = false;
				this.setChanged();
				this.notifyObservers(DebuggerModel.ARG_STATE);
				this.stepInto(this.currentFile);
			}
		} else {
			this.setState(DebuggerModel.PAUSED);
		}
		return false;
	}

	private void step(int stepDepth) {
		if (this.suspendedThread == null) {
			return;
		}
		if (this.stepRequest != null) {
			this.eventRequestManager.deleteEventRequest(this.stepRequest);
		}
		this.stepRequest = this.eventRequestManager.createStepRequest(this.suspendedThread, StepRequest.STEP_LINE,
				stepDepth);
		for (int i = 0; i < this.lockedRefs.size(); i++) {
			this.stepRequest.addClassExclusionFilter(((ReferenceType) this.lockedRefs.get(i)).name());
		}
		this.stepRequest.addClassExclusionFilter("java.*");
		this.stepRequest.addClassExclusionFilter("javax.*");
		this.stepRequest.addClassExclusionFilter("sun.*");
		this.stepRequest.addClassExclusionFilter(".hamsterEvaluation.*");
		this.stepRequest.addCountFilter(1);
		this.stepRequest.enable();
		ThreadReference st = this.suspendedThread;
		this.suspendedThread = null;
		this.machine.resume();
	}

	public void setState(int state) {
		this.state = state;
		this.setChanged();
		if (java.awt.EventQueue.isDispatchThread()) {
			this.notifyObservers(DebuggerModel.ARG_STATE);
		}
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) {
			return;
		}
		this.enabled = enabled;
		if (this.state != DebuggerModel.NOT_RUNNING) {
			if (enabled) {
				this.runner.setDelay(0);
				this.stepInto(this.currentFile);
			} else {
				this.runner.setDelay(this.delay);
				if (this.state == DebuggerModel.RUNNING) {
					this.machine.resume();
				}
			}
		}
		this.setChanged();
		this.notifyObservers(DebuggerModel.ARG_ENABLE);
	}

	public void setDelay(int delay) {
		this.delay = delay;
		if (this.runner != null && !this.enabled) {
			this.runner.setDelay(delay);
		}
	}

	public int getDelay() {
		return this.delay;
	}

	public Thread getHamsterThread() {
		return this.hamsterThread;
	}

}