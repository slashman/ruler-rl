package net.slashie.ruler.procedural.level;

import java.util.Hashtable;

import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.domain.world.WorldTile;
import net.slashie.serf.levelGeneration.StaticGenerator;
import net.slashie.utils.Position;
import net.slashie.utils.Util;
import net.slashie.utils.ca.CARandomInitializer;
import net.slashie.utils.ca.CARule;
import net.slashie.utils.ca.Matrix;
import net.slashie.utils.ca.SZCA;

public class OverworldGenerator extends StaticGenerator{
	private int xsize, ysize;
	//private int[] dungeons, towns;
	
	private final int
		GRASS = 0,
		WATER = 1,
		FOREST = 2,
		MOUNTAIN = 3,
		DESERT = 4,
		FORESTDUNGEON = 5,
		DESERTDUNGEON = 6,
		RUINSDUNGEON = 7,
		KAKARIKO = 8,
		LINKS = 9,
		SHALLOW_WATER = 10,
		FOREST_TREE = 11,
		CACTUS = 12,
		PLAINS_TREE = 13,
		ANCIENTTEMPLE = 14,
		BUSH = 15,
		HEARTCONTAINER = 16;
	
	private void setup(){
		xsize = 128;
		ysize = 128;
	}
	
	public WorldLevel generateLevel(){
		int[][] matrix = generateLevelMatrix();
		String[] strCells = new String[matrix[0].length];
		for (int y = 0; y < matrix.length; y++){
			strCells[y] = "";
			for (int x = 0; x < matrix[0].length; x++) {
				String strCell = null;
				switch (matrix[x][y]){
				case GRASS:
					strCell = "."; break;
				case FOREST:
					strCell = "&"; break;
				case MOUNTAIN:
					strCell = "^"; break;
				case DESERT:
					strCell = "x"; break;
				case WATER:
					strCell = "~"; break;
				case FORESTDUNGEON:
					strCell = "1"; break;
				case DESERTDUNGEON:
					strCell = "2"; break;
				case RUINSDUNGEON:
					strCell = "3"; break;
				case KAKARIKO:
					strCell = "4"; break;
				case LINKS:
					strCell = "5"; break;
				case SHALLOW_WATER:
					strCell = "s"; break;
				case FOREST_TREE:
					strCell = "f"; break;
				case CACTUS:
					strCell = "c"; break;
				case PLAINS_TREE:
					strCell = "p"; break;
				case ANCIENTTEMPLE:
					strCell = "6"; break;
				case BUSH:
					strCell = "*"; break;
				case HEARTCONTAINER:
					strCell = "H"; break;
				}
				strCells[y] += strCell;
			}
		}
		WorldLevel level = new WorldLevel();
		level.setCells(new WorldTile[1][xsize][ysize]);
		StaticGenerator.getGenerator().renderOverLevel(level, strCells, charMap, new Position(0, 0, 0));
		
		return level;
	}
	
	private Hashtable charMap = new Hashtable();
	{
		charMap.put(".", "GRASS");
		charMap.put("*", "GRASS FEATURE BUSH");
		charMap.put("&", "FOREST");
		charMap.put("^", "MOUNTAIN");
		charMap.put("x", "DESERT");
		charMap.put("~", "LAKE");
		charMap.put("1", "FORESTDUNGEON EXIT_FEATURE FORESTDUNGEON FORESTDUNGEON");
		charMap.put("2", "DESERTDUNGEON EXIT_FEATURE DESERTDUNGEON DESERTDUNGEON");
		charMap.put("3", "RUINSDUNGEON EXIT_FEATURE RUINSDUNGEON RUINSDUNGEON");
		charMap.put("4", "KAKARIKO FEATURE KAKARIKO");
		charMap.put("5", "LINKS EXIT_FEATURE LINKS LINKS");
		charMap.put("6", "ANCIENTTEMPLE EXIT_FEATURE ANCIENTTEMPLE ANCIENTTEMPLE");
		charMap.put("s", "SHALLOW_WATER");
		charMap.put("f", "FOREST_TREE");
		charMap.put("c", "CACTUS");
		charMap.put("p", "PLAINS_TREE");
		charMap.put("H", "MOUNTAIN FEATURE HEARTCONTAINER");
	}
	private Position dungeon1Pos = null;
	private Position dungeon2Pos = null;
	private Position dungeon3Pos = null;
	private Position ancientPos = null;
	private Position kakarikoPos = null;
	private Position linkPos = null;
	
	public int[][] generateLevelMatrix(){
		setup();
		/** Uses ZeldaOverworld Cellular automata, by SZ
		 
		 */
		
		CARandomInitializer vInit = new CARandomInitializer(new double [] {0.65, 0.0, 0.05, 0.2}, MOUNTAIN);
		//GRASS, WATER, FOREST, MOUNTAIN, DESERT,
		CARule [] vRules = new CARule []{
			new CARule(GRASS, CARule.MORE_THAN, 2, FOREST, FOREST),
			new CARule(GRASS, CARule.MORE_THAN, 2, WATER, WATER),
			new CARule(GRASS, CARule.MORE_THAN, 1, DESERT, DESERT),
			new CARule(GRASS, CARule.MORE_THAN, 3, MOUNTAIN, MOUNTAIN),
			
			new CARule(WATER, CARule.MORE_THAN, 5, GRASS, GRASS),
			
			//new CARule(DESERT, CARule.MORE_THAN, 3, DESERT, GRASS),
			new CARule(DESERT, CARule.MORE_THAN, 0, WATER, GRASS),
			new CARule(DESERT, CARule.MORE_THAN, 5, GRASS, GRASS),
			new CARule(DESERT, CARule.MORE_THAN, 7, MOUNTAIN, MOUNTAIN),
			
			new CARule(FOREST, CARule.MORE_THAN, 4, MOUNTAIN, MOUNTAIN),
			
			new CARule(MOUNTAIN, CARule.LESS_THAN,1, MOUNTAIN, GRASS),
			new CARule(DESERT, CARule.LESS_THAN, 1, DESERT, GRASS),
			new CARule(WATER, CARule.LESS_THAN, 1, WATER, GRASS),
			new CARule(FOREST, CARule.LESS_THAN, 1, FOREST, GRASS),
			
			new CARule(GRASS, CARule.MORE_THAN, 0, DESERTDUNGEON, DESERT),
			new CARule(FOREST, CARule.MORE_THAN, 0, DESERTDUNGEON, DESERT),
			new CARule(MOUNTAIN, CARule.MORE_THAN, 0, DESERTDUNGEON, DESERT),
			new CARule(WATER, CARule.MORE_THAN, 0, DESERTDUNGEON, DESERT),
			
			
			new CARule(MOUNTAIN, CARule.MORE_THAN, 0, FORESTDUNGEON, GRASS),
			//new CARule(MOUNTAIN, CARule.MORE_THAN, 0, DESERTDUNGEON, GRASS),
			new CARule(MOUNTAIN, CARule.MORE_THAN, 0, RUINSDUNGEON, GRASS),
			new CARule(MOUNTAIN, CARule.MORE_THAN, 0, KAKARIKO, GRASS),
			new CARule(MOUNTAIN, CARule.MORE_THAN, 0, LINKS, GRASS),
		};
		Matrix map = new Matrix(xsize,ysize);
		vInit.init(map);
		int [][] ret = map.getArrays();
		
		
		out: do {
			dungeon1Pos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			dungeon2Pos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			dungeon3Pos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			ancientPos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			kakarikoPos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			linkPos = new Position(Util.rand(20,xsize-20), Util.rand(20,ysize-20));
			Position[] checks = new Position[]{dungeon1Pos,dungeon2Pos,dungeon3Pos,kakarikoPos, linkPos, ancientPos};
			for (int i = 0; i < checks.length; i++){
				if (ret[checks[i].x][checks[i].y] == WATER)
					continue out;
				in: for (int j = 0; j < checks.length; j++){
					if (i == j)
						continue in;
					if (Position.distance(checks[i], checks[j]) < 40){
						continue out;
					}
				}
			}
			break;
		} while (true);
		
		map.addShoweredHotSpot(DESERT, DESERT, 200, 70);
		map.addShoweredHotSpot(WATER, WATER, 200, 20);
		map.addShoweredHotSpot(WATER, WATER, 400, 20);
		map.addShoweredHotSpot(WATER, WATER, 400, 30);
		map.addShoweredHotSpot(FOREST, FOREST, 300, 10);
		map.addShoweredHotSpot(dungeon1Pos, DESERTDUNGEON, DESERT, 300, 15);
		map.addShoweredHotSpot(dungeon2Pos, FORESTDUNGEON, FOREST, 400, 20);
		map.addHotSpot(dungeon3Pos, RUINSDUNGEON);
		map.addHotSpot(ancientPos, ANCIENTTEMPLE);
		map.addHotSpot(kakarikoPos, KAKARIKO);
		map.addHotSpot(linkPos, LINKS);
		
		SZCA.runCA(map, vRules, 10, false);
		
		CARule [] cleanupRules = new CARule []{
				new CARule(MOUNTAIN, CARule.LESS_THAN,2, MOUNTAIN, GRASS),
				//new CARule(DESERT, CARule.LESS_THAN, 2, DESERT, GRASS),
				//new CARule(WATER, CARule.LESS_THAN, 2, WATER, GRASS),
				new CARule(FOREST, CARule.LESS_THAN, 2, FOREST, GRASS),
			};
		SZCA.runCA(map, cleanupRules, 1, false);
		
		cleanupRules = new CARule []{
				new CARule(GRASS, CARule.MORE_THAN, 0, DESERT, DESERT),
				new CARule(GRASS, CARule.MORE_THAN, 0, FOREST, FOREST),
				new CARule(GRASS, CARule.MORE_THAN, 0, WATER, WATER),
			};
		SZCA.runCA(map, cleanupRules, 1, false);
		
		CARule [] detailRules = new CARule []{
			new CARule(WATER, CARule.MORE_THAN, 2, GRASS, SHALLOW_WATER),
			new CARule(WATER, CARule.MORE_THAN, 2, DESERT, SHALLOW_WATER),
			new CARule(WATER, CARule.MORE_THAN, 2, MOUNTAIN, SHALLOW_WATER),
			new CARule(WATER, CARule.MORE_THAN, 2, FOREST, SHALLOW_WATER),
			new CARule(WATER, CARule.MORE_THAN, 2, SHALLOW_WATER, SHALLOW_WATER),
		};
		SZCA.runCA(map, detailRules, 3, false);
		
		// Trees, Cactuses, Bushes
		
		int trees = (int)((xsize*ysize)/25.0D);
		for (int i = 0; i < trees; i++){
			int x = Util.rand(5, xsize-5);
			int y = Util.rand(5, ysize-5);
			if (ret[x][y] == FOREST)
				ret[x][y] = FOREST_TREE;
			else
				i--;
		}
		int cactus = (int)((xsize*ysize)/300.0D);
		for (int i = 0; i < cactus; i++){
			int x = Util.rand(5, xsize-5);
			int y = Util.rand(5, ysize-5);
			if (ret[x][y] == DESERT)
				ret[x][y] = CACTUS;
			else
				i--;
		}
		trees = (int)((xsize*ysize)/50.0D);
		for (int i = 0; i < trees; i++){
			int x = Util.rand(5, xsize-5);
			int y = Util.rand(5, ysize-5);
			if (ret[x][y] == GRASS)
				ret[x][y] = PLAINS_TREE;
			else
				i--;
		}
		
		trees = (int)((xsize*ysize)/200.0D);
		for (int i = 0; i < trees; i++){
			int x = Util.rand(5, xsize-5);
			int y = Util.rand(5, ysize-5);
			if (ret[x][y] == GRASS)
				ret[x][y] = BUSH;
			else
				i--;
		}
		
		trees = 10;
		for (int i = 0; i < trees; i++){
			int x = Util.rand(5, xsize-5);
			int y = Util.rand(5, ysize-5);
			if (ret[x][y] == MOUNTAIN)
				ret[x][y] = HEARTCONTAINER;
			else
				i--;
		}
		
		return ret;
	}
	
}
