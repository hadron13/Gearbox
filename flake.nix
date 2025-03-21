{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:

  let
    pkgs = nixpkgs.legacyPackages.x86_64-linux;
    libs = with pkgs; [
        libpulseaudio
        libGL
        glfw
        openal
        xorg.libX11
        stdenv.cc.cc.lib
      ];
  in {
    devShell.x86_64-linux = pkgs.mkShell {
      packages = [];
      buildInputs = libs;
      LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;
    };
  };
}
