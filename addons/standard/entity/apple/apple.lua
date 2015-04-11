local apple = {

	name = "apple",
	description = "Rumored to repell doctors.",
	
	id = "apple",
	
	radius = 1,
	
	canBePlaced = true,
	
	model = fromFile("entity/apple/apple.j3o")
}

local mat = apple.model.material;

mat.shading = true;
mat.ambientColor = color(1, 1, 1)
mat.diffuseColor = color(1, 1, 1)
mat.texture = fromFile("entity/apple/apple.png");

return apple;