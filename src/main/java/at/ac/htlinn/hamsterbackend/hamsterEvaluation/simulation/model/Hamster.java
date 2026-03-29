package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;

/**
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class Hamster {
	protected int id;

	protected int x;
	protected int y;
	protected int dir;
	protected int mouth;
	protected int color = Utils.COLOR;

	public Hamster(int id, int x, int y, int dir, int mouth, int c) {
		this(id);
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.mouth = mouth;
		this.color = c;
	}

	public Hamster(int id) {
		this.id = id;
		this.dir = 1;
	}

	public Hamster(Hamster h) {
		this(h.id, h.x, h.y, h.dir, h.mouth, h.color);
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDir() {
		return dir;
	}

	public int getMouth() {
		return mouth;
	}

	public int getColor() {
		return color;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public void setMouth(int mouth) {
		this.mouth = mouth;
	}
}
