
Addon = {
	name = "Standard Content",
	description = "Standard stuff that's supposed to be in the game. It's 'vanilla.'",

	id = "vsv",
	author = "Naftoreiclag",

	version = 1,

	downgrades = {
		
	},
}

Addon.entities = {
	fromFile("entity/apple/apple.lua")
}

return Addon
