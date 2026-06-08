package com.carboncredit.app.core.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfGenerator {

    fun generatePdf(context: Context, content: String): Uri? {
        try {
            val document = PdfDocument()

            // Page settings (A4 approximate)
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            var page = document.startPage(pageInfo)
            var canvas = page.canvas

            val textPaint = TextPaint().apply {
                color = Color.BLACK
                textSize = 12f
                typeface = Typeface.MONOSPACE
                isAntiAlias = true
            }

            // Using StaticLayout to handle text wrapping
            val textWidth = pageInfo.pageWidth - 80 // 40 margin on each side
            
            // For older API compat, we use the deprecated constructor
            @Suppress("DEPRECATION")
            val staticLayout = StaticLayout(
                content, textPaint, textWidth,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false
            )

            // Draw content to pages, handling overflow
            var yOffset = 40f
            var startLine = 0

            while (startLine < staticLayout.lineCount) {
                // Determine how many lines fit on the current page
                var endLine = startLine
                var heightUsed = 0f
                val availableHeight = pageInfo.pageHeight - 80f // 40 top/bottom margins

                while (endLine < staticLayout.lineCount && heightUsed + staticLayout.getLineBottom(endLine) - staticLayout.getLineTop(startLine) <= availableHeight) {
                    endLine++
                }

                // If no lines fit (extremely rare, huge font), just put one line
                if (endLine == startLine) endLine++

                // Create a smaller StaticLayout just for this page's text slice
                val pageText = content.substring(
                    staticLayout.getLineStart(startLine),
                    if (endLine < staticLayout.lineCount) staticLayout.getLineStart(endLine) else content.length
                )

                @Suppress("DEPRECATION")
                val pageLayout = StaticLayout(
                    pageText, textPaint, textWidth,
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false
                )

                canvas.save()
                canvas.translate(40f, 40f)
                pageLayout.draw(canvas)
                canvas.restore()

                document.finishPage(page)
                startLine = endLine

                // Create a new page if more lines are left
                if (startLine < staticLayout.lineCount) {
                    page = document.startPage(pageInfo)
                    canvas = page.canvas
                }
            }

            // Ensure reports directory exists
            val reportsDir = File(context.cacheDir, "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(reportsDir, "Audit_Report_${timestamp}.pdf")

            document.writeTo(FileOutputStream(file))
            document.close()

            // Return content Uri
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
