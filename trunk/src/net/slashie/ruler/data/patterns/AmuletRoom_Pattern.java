package net.slashie.ruler.data.patterns;

import net.slashie.serf.level.Unleasher;
import net.slashie.serf.levelGeneration.StaticPattern;

public class AmuletRoom_Pattern extends StaticPattern {

	public AmuletRoom_Pattern () {
		this.cellMap = new String [][]{{
			"#.......############.",
			"#########..........##",
			"S...................#",
			"#########..........##",
			"#.......############.",
			
		}};
		
		charMap.put(".", "FLOOR");
		charMap.put("#", "WALL");
		charMap.put("S", "FLOOR EXIT _BACK");

		unleashers = new Unleasher[]{};
	}

	@Override
	public String getDescription() {
		return "Teh Amulet Room";
	}

}
