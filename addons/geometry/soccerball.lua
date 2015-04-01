futbol = {
	name = "Soccer Ball",
	description = "Fell off the back of a minivan.",
	id = "pele",
	
	radius = 1,
	
	movable = true,
	obtainable = true,
	
	model = fromFile("Icosphere.mesh.j3o")
}

local mat = futbol.model.material

mat.shading = true
mat.texture = fromFile("soccerball.jpg")
mat.matcap = true

return futbol;
