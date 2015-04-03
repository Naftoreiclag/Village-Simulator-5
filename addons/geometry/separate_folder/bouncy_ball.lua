entityBouncyBall = {
	name = "Bouncy Ball",
	description = "Tutorial completed!",
	id = "bouncyball",
	
	radius = 1,
	
	movable = true,
	obtainable = true,
	
	model = fromFile("Icosphere.mesh.j3o") -- Absolute path
}

local mat = entityBouncyBall.model.material

mat.shading = true
mat.texture = fromFile("separate_folder/bouncy_ball_red.png")
mat.matcap = true

return entityBouncyBall;
