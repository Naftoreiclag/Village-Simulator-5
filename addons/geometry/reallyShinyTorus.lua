shinyEntity = {
	name = "Shiny Thing",
	description = "Why isn't this a collectable?.",
	id = "shiny",
	
	radius = 1,
	
	model = fromFile("torus.obj")
}

local mat = shinyEntity.model.material

mat.texture = fromFile("quartz.jpg")
mat.shading = true
mat.matcap = true

return shinyEntity;
