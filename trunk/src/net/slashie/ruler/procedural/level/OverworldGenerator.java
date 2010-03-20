package net.slashie.ruler.procedural.level;

import java.util.Hashtable;

import net.slashie.serf.levelGeneration.StaticGenerator;
import net.slashie.serf.levelGeneration.cave.Wisp;
import net.slashie.serf.levelGeneration.cave.WispSim;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.domain.world.WorldTile;
import net.slashie.utils.ca.CARandomInitializer;
import net.slashie.utils.ca.CARule;
import net.slashie.utils.ca.Matrix;
import net.slashie.utils.ca.SZCA;
import net.slashie.utils.*;

public class OverworldGenerator extends StaticGenerator{
	private final int
	GRASS = 0,
	WATER = 1,
	FOREST = 2,
	MOUNTAIN = 3,
	DESERT = 4;
	
	private final char[] charTiles = new char[]{
		'.',
		'~',
		'f',
		'm',
		'd'
	};
	
	private Hashtable<String, String> table;
	public void init(String grass, String water, String forest, String mountain, String desert){
		table = new Hashtable<String, String>();
		table.put(".", grass);
		table.put("~", water);
		table.put("f", forest);
		table.put("m", mountain);
		table.put("d", desert);
		
	}
	
	public WorldLevel generateLevel(int xdim, int ydim) {
		/** Uses ZeldaOverworld Cellular automata, by SZ
		 */
		
		
		CARandomInitializer vInit = new CARandomInitializer(new double [] {0.2, 0.2, 0.03, 0.05}, MOUNTAIN);
		//GRASS, WATER, FOREST, MOUNTAIN, DESERT,
		CARule [] vRules = new CARule []{
			new CARule(GRASS, CARule.MORE_THAN, 3, FOREST, FOREST),
			new CARule(GRASS, CARule.MORE_THAN, 2, WATER, WATER),
			new CARule(GRASS, CARule.MORE_THAN, 3, DESERT, DESERT),
			new CARule(GRASS, CARule.MORE_THAN, 3, MOUNTAIN, MOUNTAIN),
			
			new CARule(WATER, CARule.MORE_THAN, 5, GRASS, GRASS),
			
			new CARule(DESERT, CARule.MORE_THAN, 2, DESERT, GRASS),
			new CARule(DESERT, CARule.MORE_THAN, 0, WATER, GRASS),
			new CARule(DESERT, CARule.MORE_THAN, 5, GRASS, GRASS),
			
			new CARule(DESERT, CARule.MORE_THAN, 7, MOUNTAIN, MOUNTAIN),
			new CARule(FOREST, CARule.MORE_THAN, 1, MOUNTAIN, MOUNTAIN),
			
			new CARule(MOUNTAIN, CARule.LESS_THAN,1, MOUNTAIN, GRASS),
			new CARule(DESERT, CARule.LESS_THAN, 1, DESERT, GRASS),
			new CARule(WATER, CARule.LESS_THAN, 1, WATER, GRASS),
			new CARule(FOREST, CARule.LESS_THAN, 1, FOREST, GRASS),
		};

		int[][] intMap = null;
		Matrix map = new Matrix(xdim,ydim);
		vInit.init(map);
		SZCA.runCA(map, vRules, 5, false);
		intMap = map.getArrays();
		
		Position start = new Position(Util.rand(1, xdim-1), Util.rand(1, ydim-1));
		Position end = new Position(Util.rand(1, xdim-1), Util.rand(1, ydim-1));
		while (Position.distance(start, end) < 10){
			start = new Position(Util.rand(1, xdim-1), Util.rand(1, ydim-1));
			end = new Position(Util.rand(1, xdim-1), Util.rand(1, ydim-1));
		}
			
		//Run the wisps
		WispSim.setWisps(new Wisp(start, 10,20,5),new Wisp(end, 10,20,5));
		WispSim.run(intMap);
		
		//Put the keys
		Position key1 = null;
		Position key2 = null;
		while (key1 == null){
			int xpos = Util.rand(0,intMap.length-1);
			int ypos = Util.rand(0,intMap[0].length-1);
			if (intMap[xpos][ypos] == 0)
				key1 = new Position(xpos, ypos);
		}
		while (key2 == null){
			int xpos = Util.rand(0,intMap.length-1);
			int ypos = Util.rand(0,intMap[0].length-1);
			if (intMap[xpos][ypos] == 0)
				key2 = new Position(xpos, ypos);
		}
		
		//Run the wisps for the keys
		WispSim.setWisps(new Wisp(key1, 40,30,3),new Wisp(key2, 20,30,3));
		WispSim.run(intMap);
		
		String[] tiles = new String[intMap.length];
		for (int x = 0; x < intMap.length; x++)
			tiles[x] = "";
		for (int x = 0; x < intMap.length; x++)
			for (int y = 0; y < intMap[0].length; y++)
				tiles[x] += charTiles[intMap[y][x]];
		WorldLevel ret = new WorldLevel();
		ret.setCells(new WorldTile[1][tiles.length][tiles[0].length()]);
		renderOverLevel(ret, tiles, table, new Position(0,0));
		
		ret.addExit(start, "_BACK");
		ret.addExit(end, "_NEXT");
		return ret;
	}
}

