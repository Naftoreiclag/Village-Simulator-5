entityCone = {
	name = "Unobtainium Cone",
	description = "One third of a cylinder. Also the reason there will be no big crunch.",
	id = "cone",
	
	radius = 1,
	
	movable = false; -- Cannot be moved
	obtainable = false; -- Cannot be put in inventory
	
	model = fromFile("cone.j3o")
}

local mat = entityCone.model.material

mat.shading = true
mat.texture = fromFile("orange.jpg")

return entityCone;
