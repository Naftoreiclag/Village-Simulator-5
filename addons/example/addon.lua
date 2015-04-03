-- This script is called once during initialization of the addon.
-- Lua is being treated here like a config file that you can add functionality to.
-- It must return a table containing all of the addon's information.

Addon = {
	-- User-friendly readable information. Can change between releases.
	name = "Example Addon",
	description = "An example addon that adds some very basic things.",

	-- What the game and all other addons will know this project as.
	-- This should not vary between releases.
	id = "myexample",
	author = "Alexander Hamilton",

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
}

-- List all of your entities here.
Addon.entities = {
	fromFile("entities/box.lua"),
	fromFile("entities/money.lua")
}

return Addon
