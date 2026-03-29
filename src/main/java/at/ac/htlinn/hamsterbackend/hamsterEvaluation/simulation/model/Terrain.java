package at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.debugger.model.IHamster;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Utils;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * @author Daniel
 */
public class Terrain {
	static final char[] DIRS = { '^', '>', 'v', '<' };

	int width;
	int height;

	boolean[][] walls;
	int[][] corn;

	/**
	 * The default hamster position.
	 */
	Hamster defaultHamster;

	public Terrain(int width, int height) {
		this.width = width;
		this.height = height;
		walls = new boolean[width][height];
		corn = new int[width][height];
		defaultHamster = new Hamster(-1);
	}

	public Terrain(String string) {
		Pattern lineSplitter = Pattern.compile("\n|\r\n");
		String[] lines = lineSplitter.split(string);
		width = Integer.parseInt(lines[0]);
		height = Integer.parseInt(lines[1]);
		walls = new boolean[width][height];
		corn = new int[width][height];
		ArrayList<int[]> cornPosition = new ArrayList<int[]>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				switch (lines[i + 2].charAt(j)) {
				case '#':
					walls[j][i] = true;
					break;
				case ' ':
					walls[j][i] = false;
					break;
				case '*':
					cornPosition.add(new int[] { j, i });
					break;
				case '^':
					cornPosition.add(new int[] { j, i });
					setDefault(j, i, 0);
					break;
				case '>':
					cornPosition.add(new int[] { j, i });
					setDefault(j, i, 1);
					break;
				case 'v':
					cornPosition.add(new int[] { j, i });
					setDefault(j, i, 2);
					break;
				case '<':
					cornPosition.add(new int[] { j, i });
					setDefault(j, i, 3);
					break;
				default:
					throw new RuntimeException("Territory error.");
				}
			}
		}
		for (int i = 0; i < cornPosition.size(); i++) {
			int[] p = (int[]) cornPosition.get(i);
			int count = Integer.parseInt(lines[2 + height + i]);
			corn[p[0]][p[1]] = count;
		}
		defaultHamster.setMouth(Integer.parseInt(lines[2 + height
				+ cornPosition.size()]));
	}

	public Terrain(Terrain t) {
		this(t.getWidth(), t.getHeight());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				walls[i][j] = t.walls[i][j];
				corn[i][j] = t.corn[i][j];
			}
		}
		defaultHamster = new Hamster(t.defaultHamster);
	}

	private void setDefault(int x, int y, int dir) {
		defaultHamster = new Hamster(-1, x, y, dir, 0,
				IHamster.$i$defColor);
	}

	private boolean inside(int row, int col) {
		return col >= 0 && row >= 0 && col < width && row < height;
	}

	public boolean getWall(int col, int row) {
		if (!inside(row, col))
			return true;
		return walls[col][row];
	}

	public void setWall(int x, int y, boolean value) {
		if (inside(y, x))
			walls[x][y] = value;
	}

	public Hamster getDefaultHamster() {
		return defaultHamster;
	}

	public int getCornCount(int x, int y) {
		if (!inside(y, x))
			return 0;
		return corn[x][y];
	}

	public void setCornCount(int x, int y, int count) {
		if (inside(y, x))
			corn[x][y] = count;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(width);
		result.append(Utils.LSEP);
		result.append(height);
		result.append(Utils.LSEP);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (defaultHamster.getX() == j && defaultHamster.getY() == i) {
					result.append(DIRS[defaultHamster.getDir()]);
				} else if (walls[j][i]) {
					result.append('#');
				} else if (corn[j][i] != 0) {
					result.append('*');
				} else {
					result.append(' ');
				}
			}
			result.append(Utils.LSEP);
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (defaultHamster.getX() == j && defaultHamster.getY() == i) {
					result.append(corn[j][i]);
					result.append(Utils.LSEP);
				} else if (!walls[j][i] && corn[j][i] != 0) {
					result.append(corn[j][i]);
					result.append(Utils.LSEP);
				}
			}
		}
		result.append(defaultHamster.getMouth());
		result.append(Utils.LSEP);
		return new String(result);
	}
}