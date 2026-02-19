package com.example.quicklinks

import com.example.quicklinks.generators.DesktopFileGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

class DesktopFileGeneratorTest {

    var name: String = "QuicklinkName"
    var url: String = "URL"
    var iconPath: String = "IconPath"

    @Test
    fun getDesktopStringReturnsUsableDesktopString(){
        assertEquals(
            "[Desktop Entry]\n" +
                    "Name=${name}\n" +
                    "Comment=Open ${name}\n" +
                    "Exec=xdg-open ${url}\n" +
                    "Icon=${iconPath}\n" +
                    "Terminal=false\n" +
                    "Type=Application\n" +
                    "Categories=Quicklink;",
            DesktopFileGenerator.getDesktopFileString(name, url, iconPath),
            "Desktop String will not Work like this"
        )
    }
}