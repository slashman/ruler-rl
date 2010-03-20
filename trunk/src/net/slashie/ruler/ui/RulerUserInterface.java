package net.slashie.ruler.ui;

import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.util.Pair;

public interface RulerUserInterface {
	public Unit selectUnitFrom(String prompt, List<Unit> list) throws ActionException ;

	public Specialist selectSpecialistFrom(String string, List<Specialist> availableSpecialists) throws ActionException ;

	public Resource selectResourceFrom(String string, List<Pair<Resource, Integer>> availableResources) throws ActionException ;
}
