#version 330 core

in vec2 TexCoord;

out vec4 color;

uniform sampler2D mainTexture;
uniform sampler2D normalMapTexture;

void main()
{
	color = mix(texture(mainTexture,TexCoord), texture(normalMapTexture,TexCoord),0.5f);
}