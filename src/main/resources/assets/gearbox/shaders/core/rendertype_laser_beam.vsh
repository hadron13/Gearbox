#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV2;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord1;
out vec4 normal;
out vec3 position;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    position = (ModelViewMat * vec4(Position, 1.0)).xyz;//gl_Position.xyz;
    vertexColor = Color;
    texCoord0 = UV0;
    texCoord1 = UV2;
    normal = vec4(Normal, 0.0f);//ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
