#version 330 core

struct PhongLight{
	vec3 position;
	vec3 phongLightColor;
	float specularStrength;
	float diffuseStrength;
	
	float linear;
	float quadratic;
};

out vec4 color;

in vec3 myColor;
in vec3 myNormal;
in vec3 fragPos;

#define NUM_LIGHTS 6

uniform PhongLight phongLights[NUM_LIGHTS];
uniform vec3 ambientLight;
uniform vec3 cameraPos;

vec3 calculatePhongLight(PhongLight phongLight, vec3 normal, vec3 fragPos, vec3 cameraPos);

void main()
{
	vec3 norm = normalize(myNormal);
	vec3 result = vec3(0.0f,0.0f,0.0f);
	result = (result + ambientLight);
	for(int i=0; i<NUM_LIGHTS; i++){
		result = result + calculatePhongLight(phongLights[i], norm, fragPos, cameraPos);
	}	
	result = result*myColor;
    color = vec4(result,1.0f);
}

vec3 calculatePhongLight(PhongLight phongLight, vec3 normal, vec3 fragPos, vec3 cameraPos){
	vec3 diffuseColor = vec3(0.0f,0.0f,0.0f);
	vec3 specular = vec3(0.0f,0.0f,0.0f);
	vec3 lightDir = normalize(phongLight.position - fragPos);
	float diff = max(dot(normal, lightDir),0.0f);
	diffuseColor = diff*(phongLight.diffuseStrength)*(phongLight.phongLightColor);
	vec3 viewDir = normalize(cameraPos-fragPos);
	vec3 reflectDir = reflect(-lightDir,normal);
	float spec = pow(max(dot(viewDir,reflectDir),0.0f),32);
	specular = spec*(phongLight.specularStrength)*(phongLight.phongLightColor);
	
	
	
	float distanceToLight = length(fragPos-phongLight.position);
	float finalIntensity = phongLight.diffuseStrength/(1.0f+phongLight.linear*distanceToLight+phongLight.quadratic*distanceToLight*distanceToLight);
	
	
	return finalIntensity*(diffuseColor + specular);
}

 