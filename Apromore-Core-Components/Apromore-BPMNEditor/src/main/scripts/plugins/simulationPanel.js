/**
 * @namespace Oryx name space for plugins
 * @name ORYX.Plugins
 */
if (!ORYX.Plugins)
    ORYX.Plugins = new Object();

/**
 * The simulation panel plugin offers functionality to change model simulation parameters over the
 * simulation parameters panel.
 *
 * @class ORYX.Plugins.SimulationPanel
 * @extends Clazz
 * @param {Object} facade The editor facade for plugins.
 */
ORYX.Plugins.SimulationPanel = Clazz.extend({
    /** @lends ORYX.Plugins.SimulationPanel.prototype */
    facade: undefined,

    construct: function (facade) {
        this.facade = facade;

        /* Register toggle simulation panel */
        this.facade.offer({
            'name': ORYX.I18N.SimulationPanel.toggleSimualtionDrawer,
            'functionality': this.toggleSimulationDrawer.bind(this),
            'group': ORYX.I18N.SimulationPanel.group,
            'description': ORYX.I18N.SimulationPanel.toggleSimualtionDrawerDesc,
            'index': 1,
            'minShape': 0,
            'maxShape': 0
        });
    },

    /**
     * Shortcut for performing an expand or collapse based on the current state of the panel.
     */
    toggleSimulationDrawer: function () {
        this.facade.getSimulationDrawer().toggleCollapse(true);
    }
});