entityBouncyBall = {
	name = "Bouncy Ball",
	description = "Tutorial completed!",
	id = "bouncyball",
	
	radius = 1,
	
	movable = true,
	obtainable = true,
	
	model = fromFile("Icosphere.mesh.j3o")
}

local mat = entityBouncyBall.model.material

mat.shading = true
mat.texture = fromFile("bouncy_ball_red.png")
mat.matcap = true

return entityBouncyBall;
