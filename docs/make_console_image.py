#!/usr/bin/env python3
"""
Renders sample_output.txt (the captured stdout of ProjectManagementDemo) into
console_output.png, the figure used in the report.

Regenerate sample_output.txt with:
    java -cp out com.pms.app.ProjectManagementDemo > docs/sample_output.txt
"""
import os
from PIL import Image, ImageDraw, ImageFont

HERE = os.path.dirname(os.path.abspath(__file__))
SRC = os.path.join(HERE, "sample_output.txt")
OUT = os.path.join(HERE, "console_output.png")

FONT_CANDIDATES = [
    "/System/Library/Fonts/Menlo.ttc",
    "/System/Library/Fonts/Monaco.ttf",
    "/System/Library/Fonts/Supplemental/Courier New.ttf",
    "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf",
]


def pick_font(size=15):
    for path in FONT_CANDIDATES:
        try:
            return ImageFont.truetype(path, size)
        except OSError:
            continue
    return ImageFont.load_default()


def colour_for(line):
    if "[PASS]" in line:
        return "#5fd75f"
    if "[FAIL]" in line:
        return "#ff6b6b"
    if line.startswith(("=", "-")):
        return "#6b7089"
    if any(k in line for k in ("RESULT", "TOTAL", "TEST CASES", "DEMO SCENARIO")):
        return "#7dcfff"
    if line.strip().startswith(("WebApplicationProject", "MobileAppProject")):
        return "#ffd866"
    return "#e0e0e8"


def main():
    lines = open(SRC, encoding="utf-8").read().rstrip("\n").split("\n")
    font = pick_font()
    pad, line_h, title_h = 22, 21, 34
    char_w = font.getbbox("M")[2]

    width = pad * 2 + int(char_w * max(len(l) for l in lines)) + 10
    height = title_h + pad * 2 + line_h * len(lines)

    img = Image.new("RGB", (width, height), "#1e1e2e")
    d = ImageDraw.Draw(img)

    # title bar
    d.rectangle([0, 0, width, title_h], fill="#2d2d3f")
    for i, colour in enumerate(["#ff5f56", "#ffbd2e", "#27c93f"]):
        d.ellipse([16 + i * 22, 11, 28 + i * 22, 23], fill=colour)
    d.text((width // 2 - 140, 8), "Terminal - ProjectManagementDemo",
           font=font, fill="#c8c8d8")

    y = title_h + pad
    for line in lines:
        d.text((pad, y), line, font=font, fill=colour_for(line))
        y += line_h

    img.save(OUT)
    print("wrote", OUT, img.size)


if __name__ == "__main__":
    main()
