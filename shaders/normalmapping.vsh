#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uvCoords;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;
layout (location = 4) in vec3 biTangent;

out vec3 fragPos;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec2 TexCoord;

void main()
{
	gl_Position = projection*view*model*vec4(position,1.0f);
	fragPos = vec3(model * vec4(position,1.0f));
	TexCoord = uvCoords;
}