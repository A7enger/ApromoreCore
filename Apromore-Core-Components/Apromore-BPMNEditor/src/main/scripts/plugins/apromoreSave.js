/**
 * Copyright (c) 2006
 * Martin Czuchra, Nicolas Peters, Daniel Polak, Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/
if(!ORYX){ var ORYX = {} }
if(!ORYX.Plugins){ ORYX.Plugins = {} }

ORYX.Plugins.ApromoreSave = Clazz.extend({

    facade:undefined,

    changeSymbol:"*",

    construct:function (facade) {
        this.facade = facade;

        this.facade.offer({
            'name':ORYX.I18N.Save.save,
            'functionality':this.save.bind(this, false),
            'group':ORYX.I18N.Save.group,
            'icon':ORYX.PATH + "images/disk.png",
            'description':ORYX.I18N.Save.saveDesc,
            'index':1,
            'minShape':0,
            'maxShape':0,
            keyCodes:[
                {
                    metaKeys:[ORYX.CONFIG.META_KEY_META_CTRL],
                    keyCode:83, // s-Keycode
                    keyAction:ORYX.CONFIG.KEY_ACTION_UP
                }
            ]
        });

        // document.addEventListener("keydown", function (e) {
        //     if (e.ctrlKey && e.keyCode === 83) {
        //         Event.stop(e);
        //     }
        // }, false);


        this.facade.offer({
            'name':ORYX.I18N.Save.saveAs,
            'functionality':this.save.bind(this, true),
            'group':ORYX.I18N.Save.group,
            'icon':ORYX.PATH + "images/disk_multi.png",
            'description':ORYX.I18N.Save.saveAsDesc,
            'index':2,
            'minShape':0,
            'maxShape':0
        });

        // window.onbeforeunload = this.onUnLoad.bind(this);
        // this.changeDifference = 0;
        //
        // // Register on event for executing commands --> store all commands in a stack
        // this.facade.registerOnEvent(ORYX.CONFIG.EVENT_UNDO_EXECUTE, function () {
        //     this.changeDifference++;
        //     this.updateTitle();
        // }.bind(this));
        // this.facade.registerOnEvent(ORYX.CONFIG.EVENT_EXECUTE_COMMANDS, function () {
        //     this.changeDifference++;
        //     this.updateTitle();
        // }.bind(this));
        // this.facade.registerOnEvent(ORYX.CONFIG.EVENT_UNDO_ROLLBACK, function () {
        //     this.changeDifference--;
        //     this.updateTitle();
        // }.bind(this));

    },

    updateTitle:function () {

    },

    onUnLoad:function () {

    },

    /**
     * Saves the current process to the server.
     */
    save:function (forceNew, event) {
        if (this.saving) {
            return false;
        }

        this.saving = true;

        var xml = this.facade.getXML();
        var svg = this.facade.getSVG();

        if (forceNew) {
            if (ORYX.Plugins.ApromoreSave.apromoreSaveAs) {
                ORYX.Plugins.ApromoreSave.apromoreSaveAs(xml, svg);
            } else {
                alert("Apromore Save As method is missing!");
            }
        } else {
            if (ORYX.Plugins.ApromoreSave.apromoreSave) {
                ORYX.Plugins.ApromoreSave.apromoreSave(xml, svg);
            } else {
                alert("Apromore Save method is missing!");
            }
        }

        this.saving = false;
        return true;
    }

});


