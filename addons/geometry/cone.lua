entityCone = {
	name = "Cone",
	description = "One third of a cylinder.",
	id = "cone",
	
	radius = 1,
	
	model = fromFile("cone.j3o")
}

entityCone.model.material.texture = fromFile("orange.jpg")

return entityCone;
