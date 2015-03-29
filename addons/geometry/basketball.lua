basketball = {
	name = "Basket Ball",
	description = "Recovered from the most recent UFO crash landing.",
	id = "basketball",
	
	radius = 1,
	
	model = fromFile("Icosphere.mesh.j3o")
}

local mat = basketball.model.material

mat.shading = true
mat.texture = fromFile("basketball.jpg")
mat.ambientColor = color(1, 1, 1)
mat.diffuseColor = color(1, 1, 1)

return basketball;
