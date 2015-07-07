#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec4 color;
out vec3 myColor;
out vec3 myNormal;
out vec3 fragPos;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 normalMatrix;

void main()
{
	gl_Position = projection*view*model*vec4(position,1.0f);
	fragPos = vec3(model * vec4(position,1.0f));
	myColor = color.xyz;
	myNormal = mat3(normalMatrix) * normal; 
}