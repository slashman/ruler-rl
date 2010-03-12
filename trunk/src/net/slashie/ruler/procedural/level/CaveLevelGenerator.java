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

public class CaveLevelGenerator extends StaticGenerator{
	
	private Hashtable<String, String> table;
	public void init(String baseWall, String baseFloor){
		table = new Hashtable<String, String>();
		table.put(".", baseFloor);
		table.put("#", baseWall);
		
	}
	
	public WorldLevel generateLevel(int xdim, int ydim) {
		/** Uses Cave Cellular automata, by SZ
		 * Init (1) 30%
		 * If 0 and more than 3 1 around, 1
		 * If 1 and less than 2 1 around, 0
		 * 
		 * Run for 5 turns
		 */
		
		CARandomInitializer vInit = new CARandomInitializer(new double [] {0.3}, true);
		CARule [] vRules = new CARule []{
				new CARule(0, CARule.MORE_THAN, 3, 1, 1),
				new CARule(1, CARule.LESS_THAN, 2, 1, 0)
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
				if (intMap[y][x] == 0)
					tiles[x] += ".";
				else if (intMap[y][x] == 1)
					tiles[x] += "#";
				else if (intMap[y][x] == 4)
					tiles[x] += ".";
		WorldLevel ret = new WorldLevel();
		ret.setCells(new WorldTile[1][tiles.length][tiles[0].length()]);
		renderOverLevel(ret, tiles, table, new Position(0,0));
		
		ret.addExit(start, "_BACK");
		ret.addExit(end, "_NEXT");
		return ret;
	}
}

