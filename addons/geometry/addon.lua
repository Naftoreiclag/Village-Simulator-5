-- This script is called once during initialization of the addon.
-- Lua is being treated here like a config file that you can add functionality to.
-- It must return a table containing all of the addon's information.

Addon = {
	-- User-friendly readable information. Can change between releases.
	name = "Geometry Addon",
	description = "A really cool addon that adds some basic shapes as furniture.",

	-- What the game and all other addons will know this project as.
	-- This should not vary between releases.
	id = "naftogeometry",
	author = "Clifford the Shape Demon",

	-- Use whatever convention you want for this.
	version = 5,

	-- If world X depends on an earlier version Y of your addon and that version Y is listed here, 
	-- then world X can also accept this version of the addon instead.
	downgrades = {
		3 + 1,
		3,
		"2",
		"beta"
	},
	
	-- You can also store some custom data here.
	isAnExample = true
}

-- List all of your entities here.
Addon.entities = {
	fromFile("cone.lua"),
	fromFile("torus.lua"),
	fromFile("reallyShinyTorus.lua")
}

return Addon
