Description:
This program will go a decompiled mod (zip or jar file) and replace all obfuscated names while copying over everything else. For example, the obfuscated fields like 'field_151034_e' or 'func_130014_f_' will be replaced with the MC name APPLE and getEntityWorld, respectively (as defined by MCPBot). This can be helpful with retrieving any lost code.

# Purpose:
The purpose of this program is to aid in the retrieval of lost source code. There are up to +33,000 obfuscated fields, params and methods and having to cross check each one by hand is impractical and time-consuming. This will do the work for you.

# How to Use:
To use this program, you first need to get a decompiled version of the mod. This program does not decompile mods itself (but may do so in future versions) and will let you know if the mod you supply is not decompiled. To learn how to decompile a mod, read the How to Decompile a Mod section.

After you have your decompiled mod, press the open button and navigate to the decompiled zip or jar file and select it. The destination to save will be auto-generated, but you can specify where you want to save to by clicking Save To. Note that it will always save to the specified location and create an output folder and that the deobfuscated version will have the same name as the original.

Finally, select the mapping you want to use and click Start.

# How to Decompile a Mod:
You can use a decompiler such as ByteCodeViewer or JD-Gui, but I recommend FernFlower. More specifically, the version that Forge's modified. To build FernFlower, download and unpack the zip. Shift+Rightclick and select Open PowerShell window here and type ".\gradlew build". Once it's built, it will be in the build >> libs folder. Then follow there read me to learn about commands.

# Disclaimer:
This program comes as is: with no expressed or implied warranties.

Change Log:
- 0.1
  + A new program written in C#
  + Auto generates mapping list from http://export.mcpbot.bspk.rs/
  + Removed Lookup (may be added back later)
  + Added ReadMe
  + Herobrine can't find us here