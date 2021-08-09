import com.yahoo.platform.yui.compressor.JavaScriptCompressor



// Change to point to Bootstrap 2.0 js directory; don't forget trailing slash
def sourceJs = new File('.').absolutePath+"/src/main/"
def destJs = new File('.').absolutePath+"/src/main/resources/static/bpmneditor/editor/"


// Name of the output file; will be placed in $bootstrapJS above
def outputFile = destJs + 'apromore-editor.js'
def outputFile1 = destJs + 'apromore-editor1.js'

// order matters; popover must come after twipsy
def scripts = [
'scripts/site.properties.js',
'scripts/clazz.js',
'scripts/config.js',
'scripts/editor.js',
'scripts/editorapp.js',
'scripts/logger.js',
'scripts/server.js',
'scripts/utils.js',
'scripts/plugins/apromoreSave.js',
'scripts/plugins/export.js',
'scripts/plugins/pdf.js',
'scripts/plugins/share.js',
'scripts/plugins/simulationPanel.js',
'scripts/plugins/toolbar.js',
'scripts/plugins/undo.js',
'scripts/plugins/view.js'
]

def scripts_new = [
        'scripts/site.properties.js',
        'scripts/clazz.js',
        'scripts/config.js',
//'scripts/editor.js',
        'scripts/editorapp.js',
        'scripts/logger.js',
        'scripts/server.js',
        'scripts/utils.js',
        'scripts/plugins/apromoreSave.js',
        'scripts/plugins/export.js',
        'scripts/plugins/pdf.js',
        'scripts/plugins/share.js',
        'scripts/plugins/simulationPanel.js',
        'scripts/plugins/toolbar.js',
        'scripts/plugins/undo.js',
        'scripts/plugins/view.js'
]

def scriptText = new StringBuilder()
scripts.each { scriptText << new File(sourceJs + it).text }

def compressor = new JavaScriptCompressor( new StringReader(scriptText.toString()), null)

def compressedText = new StringWriter()

//// Params: Writer, --line-break, --nomunge, --verbose, --preserve-semi, --disable-optimizations
compressor.compress(compressedText, -1, false, false, true, true)
//
new File(outputFile).write( compressedText.toString() )

scriptText = new StringBuilder()
scripts_new.each { scriptText << new File(sourceJs + it).text }
compressor = new JavaScriptCompressor( new StringReader(scriptText.toString()), null)
compressedText = new StringWriter()
compressor.compress(compressedText, -1, false, false, true, true)
new File(outputFile1).write( compressedText.toString() )

println "Bootstrap JS files combined and minified to: ${System.properties['line.separator']}$outputFile"
