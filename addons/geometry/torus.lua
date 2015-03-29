entityTorus = {
	name = "Frosted Doughnut",
	description = "You can't eat the middle.",
	id = "torus",
	
	radius = 1,
	
	model = fromFile("torus.obj")
}

local mat = entityTorus.model.material

mat.shading = true
mat.texture = fromFile("quartz.jpg")
mat.matcap = true
mat.ambientColor = color(1, 1, 1)
mat.diffuseColor = color(1, 1, 1)

return entityTorus;
