This module contains javascript in Node.js project for the following components:
    - Process Discoverer
    - Log Animation
    - BPMN Editor (only the bundle is placed here, the BPMN Editor itself is not yet a Node project)

Folder structure:

- src: the source files divided into modules
- test: source test files divided into modules
- resources: resource files such as images, text, CSS.
- config: config files
- scripts: NPM script files, if any.
- dist: distribution bundles

Commands

npm install: to install all dependencies
npm run build: to build distribution bundles
    - processdiscoverer.js bundle: used for Process Discoverer
    - loganimationbpmn.js: used for Log Animation calling from the Portal.
npm run test: to run unit tests.

To deploy Javascript bundles to Apromore plugins:

- For Process Discoverer: drop the file dist/processdiscoverer.js into C:\apromore\ApromoreEE\ApromoreCore\Apromore-Custom-Plugins\Process-Discoverer-Portal-Plugin\src\main\resources\processdiscoverer\js\ap\ (Overwrite any existing file)

- For Log Animation: drop the file dist/loganimationbpmn.js into ApromoreEE\Log-Animation-Portal-Plugin\src\main\resources\loganimation2\js\ap (Overwrite any existing file)
- Build and deploy PD or Log Animation plugins
- Login Apromore portal, run the plugin to view the new deployment.


