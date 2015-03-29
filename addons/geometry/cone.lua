entityCone = {
	name = "Cone",
	description = "One third of a cylinder.",
	id = "cone",
	
	radius = 1,
	
	model = fromFile("cone.j3o")
}

local mat = entityCone.model.material

mat.shading = true
mat.texture = fromFile("orange.jpg")

return entityCone;
