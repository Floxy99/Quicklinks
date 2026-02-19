package com.example.quicklinks.generators

class DesktopFileGenerator {
    companion object{
        fun getDesktopFileString(name: String, url: String, iconPath: String): String {
            return "[Desktop Entry]\n" +
                    "Name=${name}\n" +
                    "Comment=Open ${name}\n" +
                    "Exec=xdg-open ${url}\n" +
                    "Icon=${iconPath}\n" +
                    "Terminal=false\n" +
                    "Type=Application\n" +
                    "Categories=Quicklink;"
        }
    }
}