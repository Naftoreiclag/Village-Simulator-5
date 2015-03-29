futbol = {
	name = "Soccer Ball",
	description = "Fell off the back of a minivan.",
	id = "futbol",
	
	radius = 1,
	
	model = fromFile("Icosphere.mesh.j3o")
}

local mat = futbol.model.material

mat.shading = true
mat.texture = fromFile("soccerball.jpg")
mat.ambientColor = color(1, 1, 1)
mat.diffuseColor = color(1, 1, 1)

return futbol;
