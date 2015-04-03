-- This is a very simple entity configuration.
-- It creates a new table called "simpleBox", specifying some key information.

simpleBox = {

	-- This is some information that can be displayed in-game.
	name = "simple box",
	description = "Scientifically known as a hexahedron.",
	
	-- Internally, this entity will be known by this id.
	id = "box",
	
	radius = 1,
	
	-- You will be able to hold this item in your inventory.
	canBePlaced = true,
	
	-- Tell the game to load your model from this file. 
	-- Note: all paths are absolute.
	model = fromFile("box/box.j3o")
}

return simpleBox;
