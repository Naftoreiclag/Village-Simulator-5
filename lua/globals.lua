-- Aaahhh (screams of terror, not a yawn)

function color (r, g, b)
	return {r, g, b}
end

function fromFile (filename)

	print("Loading " .. filename)

	-- file extensions of different length
	filetype3 = string.sub(filename, -4)
	filetype4 = string.sub(filename, -5)
	filetype5 = string.sub(filename, -6)
	filetype7 = string.sub(filename, -8)

	-- "loading" a model
	if filetype3 == ".j3o" or
	filetype3 == ".obj" or
	filetype5 == ".blend" or
	filetype8 == ".mesh.xml" then
		return {
			meshFile = filename,
			material = {}
		}
	end
	
	-- "loading" a material
	if filetype3 == ".j3m" then
		return {
			materialFile = filename
		}
	end
	
	-- "loading" a texture
	if filetype3 == ".png" or 
	filetype3 == ".jpg" or 
	filetype3 == ".bmp" or 
	filetype3 == ".tga" or 
	filetype4 == ".jpeg" or 
	filetype3 == ".gif" then
		return {
			textureFile = filename
		}
	end

	-- lua file
	if filetype3 == ".lua" then
		return dofile(ADDON_ROOT .. filename)
	end
	
	print("unknown file")
end

print("globals")
