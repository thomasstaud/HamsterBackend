package io.github.Hattinger04.hamsterEvaluation.debugger.model;


import io.github.Hattinger04.hamsterEvaluation.model.HamsterProgram;
import io.github.Hattinger04.hamsterEvaluation.model.Instruction;
import io.github.Hattinger04.hamsterEvaluation.model.InstructionProcessor;

public class LocalProcessor implements InstructionProcessor, Runnable {
	public static final String STOP_STRING = "hamster stop";
	private InstructionProcessor processor;
	private HamsterProgram program;
	private DebuggerModel debuggerModel;

	private Thread currentThread;
	private boolean paused;
	private boolean stopping;

	public LocalProcessor(InstructionProcessor processor,
			DebuggerModel debuggerModel) {
		this.processor = processor;
		this.debuggerModel = debuggerModel;
		stopping = false;
	}

	public void finished() {
		processor.finished();
	}

	public Object process(Instruction instruction) {
		synchronized (this) {
			try {
				while (paused)
					wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (stopping)
			// TODO: Implement specific Exception/Error
			throw new RuntimeException(STOP_STRING);

		Object o = null;
		try {
			o = processor.process(instruction);
			Thread.sleep(debuggerModel.getDelay());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		return o;
	}

	public void processException(Throwable e) {
		processor.processException(e);
	}

	public void start() {
		processor.start();
	}

	public void run(HamsterProgram program) {
		this.program = program;
		currentThread = new Thread(this);
		currentThread.start();
	}

	public void run() {
		start();

		try {
			
			program.main();
		} catch (NoClassDefFoundError e) {
		} catch (Exception e) {
			if (e.getMessage() == null || !e.getMessage().equals(STOP_STRING))
				e.printStackTrace();
			stopping = false;
		}
		finished();
		debuggerModel.setState(DebuggerModel.NOT_RUNNING);
	}

	public void pause() {
		paused = true;
	}

	public synchronized void resume() {
		paused = false;
		notify();
	}

	public synchronized void stop() {
		paused = false;
		stopping = true;
		notify();
	}
}
