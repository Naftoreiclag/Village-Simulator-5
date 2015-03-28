
-- User-friendly readable information. Can change between releases.
name = "Geometry Addon"
description = "A really cool addon that adds some basic shapes as furniture."

-- What the game and all other addons will know this project as.
-- This should not vary between releases.
id = "naftogeometry"
author = "Clifford the Shape Demon"

-- Use whatever convention you want for this.
version = 4 + 1

-- If world X depends on an earlier version Y of your addon and that version Y is listed here, 
-- then world X can also accept this version of the addon instead.
downgrades = {
	4,
	3,
	"beta"
}

-- List all of your .lua entities here.
entities = {
	"cone.lua",
	"cylinder.lua",
	"sphere.lua",
	"pyramid.lua"
}

print("addon loaded?")
