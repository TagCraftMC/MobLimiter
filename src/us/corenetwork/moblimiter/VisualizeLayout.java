package us.corenetwork.moblimiter;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 * Created by tux on 26.12.13.
 */
public class VisualizeLayout {
	public static final int WIDTH = 16, HEIGHT = 16;

	public static final VisualizeLayout LAYOUT_NONE = new VisualizeLayout(new String[]{
			"xx            xx",
			"x              x",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"                ",
			"x              x",
			"xx            xx"});
	public static final VisualizeLayout LAYOUT_LOW = new VisualizeLayout(new String[]{
			"xxx    xx    xxx",
			"x              x",
			"x              x",
			"                ",
			"                ",
			"                ",
			"                ",
			"x              x",
			"x              x",
			"                ",
			"                ",
			"                ",
			"                ",
			"x              x",
			"x              x",
			"xxx    xx    xxx"});
	public static final VisualizeLayout LAYOUT_MEDIUM = new VisualizeLayout(new String[]{
			"xx  xxx  xxx  xx",
			"x              x",
			"                ",
			"                ",
			"x              x",
			"x              x",
			"x              x",
			"                ",
			"                ",
			"x              x",
			"x              x",
			"x              x",
			"                ",
			"                ",
			"x              x",
			"xx  xxx  xxx  xx"});
	public static final VisualizeLayout LAYOUT_HIGH = new VisualizeLayout(new String[]{
			"x x x x  x x x x",
			"                ",
			"x              x",
			"                ",
			"x              x",
			"                ",
			"x              x",
			"                ",
			"                ",
			"x              x",
			"                ",
			"x              x",
			"                ",
			"x              x",
			"                ",
			"x x x x  x x x x"});
	public static final VisualizeLayout LAYOUT_EXCEED = new VisualizeLayout(new String[]{
			"xxxxxxxxxxxxxxxx",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"x              x",
			"xxxxxxxxxxxxxxxx"});

	boolean layout[][] = null;

	public VisualizeLayout(String [] layout) {
		this.layout = convert(layout);
	}

	public void draw(Chunk chunk, Player player, int height, int block, byte data) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (layout[x][y]) {
					player.sendBlockChange(chunk.getBlock(x, height, y).getLocation(), block, data);
				}
			}
		}
	}

	private static boolean[][] convert(String[] layout) {
		boolean r[][] = new boolean[WIDTH][];
		for (int x = 0; x < WIDTH; x++) {
			r[x] = new boolean[HEIGHT];
			for (int y = 0; y < HEIGHT; y++) {
				r[x][y] = layout[y].charAt(x) != ' ';
			}
		}

		return r;
	}
}
