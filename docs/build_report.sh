#!/usr/bin/env bash
# Regenerates report.html from the source tree and renders the final PDF.
set -e
cd "$(dirname "$0")"

CHROME="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"

echo "==> Rendering the console figure from sample_output.txt"
python3 make_console_image.py

echo "==> Generating report.html (code listings read from ../src)"
python3 build_report.py

echo "==> Rendering PDF"
"$CHROME" --headless=new --disable-gpu --no-pdf-header-footer \
          --print-to-pdf=report_raw.pdf report.html 2>/dev/null

echo "==> Numbering pages"
python3 - <<'PY'
from pypdf import PdfReader, PdfWriter
from reportlab.pdfgen import canvas
import io

src = PdfReader("report_raw.pdf")
n = len(src.pages)

def overlay(pn, w, h):
    buf = io.BytesIO()
    c = canvas.Canvas(buf, pagesize=(w, h))
    if pn >= 2:                       # cover page carries no number
        c.setFillColorRGB(0, 0, 0)
        c.setFont("Times-Roman", 11)
        c.drawCentredString(w / 2, 13 / 25.4 * 72, str(pn - 1))
    c.showPage(); c.save(); buf.seek(0)
    return PdfReader(buf).pages[0]

out = PdfWriter()
for i, page in enumerate(src.pages, start=1):
    w = float(page.mediabox.width)     # the UML page is landscape
    h = float(page.mediabox.height)
    page.merge_page(overlay(i, w, h))
    out.add_page(page)
out.add_metadata({
    "/Title": "Design and Implementation of a Project Management System",
    "/Author": "Sayanjib Sur",
    "/Subject": "Software Lab II (PG/ITE/S/121), Jadavpur University",
})
with open("Project_Management_System_Report.pdf", "wb") as f:
    out.write(f)
print("pages:", n)
PY

rm -f report_raw.pdf

PDF=Project_Management_System_Report.pdf
if command -v gs >/dev/null 2>&1; then
  echo "==> Compressing images (200 dpi) to keep the PDF under 1 MB"
  gs -q -dNOPAUSE -dBATCH -sDEVICE=pdfwrite -dCompatibilityLevel=1.7 \
     -dDetectDuplicateImages=true -dCompressFonts=true \
     -dDownsampleColorImages=true -dColorImageResolution=200 \
     -dColorImageDownsampleType=/Bicubic \
     -dAutoFilterColorImages=false -dColorImageFilter=/DCTEncode \
     -sOutputFile="$PDF.tmp" "$PDF"
  mv "$PDF.tmp" "$PDF"
else
  echo "!!  ghostscript not found - the PDF will be larger than 1 MB"
fi

BYTES=$(wc -c < "$PDF" | tr -d ' ')
printf "==> Done: docs/%s (%s bytes, %.1f KB)\n" "$PDF" "$BYTES" "$(echo "$BYTES/1024" | bc -l)"
[ "$BYTES" -lt 1000000 ] || { echo "!!  WARNING: PDF is over 1 MB"; exit 1; }
