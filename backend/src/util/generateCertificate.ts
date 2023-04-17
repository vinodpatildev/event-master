import { degrees, PDFDocument, rgb, StandardFonts } from 'pdf-lib';

const generateCertificate = async (participantName: string, eventName: string, backgroundImageFilename: string): Promise<Uint8Array> => {
  const pdfDoc = await PDFDocument.create();
  const page = pdfDoc.addPage();

  const pageWidth = page.getWidth();
  const pageHeight = page.getHeight();

  // Add background image
  const backgroundImageBytes = await fetch(backgroundImageFilename).then((res) => res.arrayBuffer());
  const backgroundImage = await pdfDoc.embedPng(backgroundImageBytes);
  page.drawImage(backgroundImage, {
    x: 0,
    y: 0,
    width: pageWidth,
    height: pageHeight,
  });

  // Add participant name
  const fontSize = 48;
  const font = await pdfDoc.embedFont(StandardFonts.HelveticaBold);
  const textWidth = font.widthOfTextAtSize(participantName, fontSize);
  page.drawText(participantName, {
    x: pageWidth / 2 - textWidth / 2,
    y: pageHeight / 2 + fontSize / 2,
    size: fontSize,
    font: font,
    color: rgb(0, 0, 0),
  });

  // Add event name
  const eventNameFontSize = 24;
  const eventNameFont = await pdfDoc.embedFont(StandardFonts.Helvetica);
  const eventNameTextWidth = eventNameFont.widthOfTextAtSize(eventName, eventNameFontSize);
  page.drawText(eventName, {
    x: pageWidth / 2 - eventNameTextWidth / 2,
    y: pageHeight / 2 - fontSize - eventNameFontSize / 2,
    size: eventNameFontSize,
    font: eventNameFont,
    color: rgb(0, 0, 0),
  });

  const pdfBytes = await pdfDoc.save();
  return pdfBytes;
};

export default generateCertificate;
