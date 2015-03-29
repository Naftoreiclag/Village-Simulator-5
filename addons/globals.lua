-- Aaahhh (screams of terror, not a yawn)

function color (r, g, b)
	return {r, g, b}
end

function fromFile (filename)

	-- file extensions of different length
	extent3 = string.sub(filename, -4)
	extent4 = string.sub(filename, -5)

	-- "loading" a model
	if extent3 == ".j3o" then
		return {
			meshFile = filename,
			material = {}
		}
	end
	
	-- "loading" a material
	if extent3 == ".j3m" then
		return {
			materialFile = filename
		}
	end
	
	-- "loading" a texture
	if extent3 == ".png" or 
	extent3 == ".jpg" or 
	extent3 == ".bmp" or 
	extent3 == ".tga" or 
	extent4 == ".jpeg" or 
	extent3 == ".gif" then
		return {
			textureFile = filename
		}
	end

	-- must be lua
	return dofile(ADDON_ROOT .. filename)
end


