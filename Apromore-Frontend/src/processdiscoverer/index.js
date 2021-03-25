import graph from './graph';
import util from './util';
import search from './search';

let PD = function(pluginExecutionId,
                  interactiveViewContainerId,
                  animationViewContainerId,
                  animationModelContainerId,
                  tokenAnimationContainerId,
                  timelineContainerId,
                  speedControlContainerId,
                  progressContainerId,
                  logInfoContainerId,
                  clockContainerId,
                  buttonsContainerId,
                  playClassName,
                  pauseClassName) {
    this._private = {
        'pluginExecutionId': pluginExecutionId,
        'interactiveViewContainerId': interactiveViewContainerId,
        'animationViewContainerId': animationViewContainerId,
        'animationModelContainerId': animationModelContainerId,
        'tokenAnimationContainerId': tokenAnimationContainerId,
        'timelineContainerId': timelineContainerId,
        'speedControlContainerId': speedControlContainerId,
        'progressContainerId': progressContainerId,
        'logInfoContainerId': logInfoContainerId,
        'clockContainerId': clockContainerId,
        'buttonsContainerId': buttonsContainerId,
        'playClassName': playClassName,
        'pauseClassName': pauseClassName
    }
}

let pdfn = PD.prototype;
[   graph,
    util,
    search
].forEach(function(props) {
    Object.assign(pdfn, props);
});

export default PD;