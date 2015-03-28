-- Aaahhh (screams of terror, not a yawn)

std_dofile = dofile

function dofile (filename)
	local f = assert(loadfile(ADDON_ROOT .. filename))
	return f()
end


