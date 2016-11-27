/**
 * init.js
 * Initializes JavaScript files
 * by calling functions on module -
 * pattern code.
 * Created by G
 */

var multiSol = multiSol || {};
multiSol.MsgRouter = multiSol.MsgRouter || {};
multiSol.form = multiSol.form || {};

multiSol.showLeftMenu();
multiSol.searchMenu();
multiSol.closeRightMenu();
multiSol.toggleRightMenu();
multiSol.enterSearch();
multiSol.form.addFeedRow();
/*multiSol.form.cancelUser();*/
/*multiSol.form.submitNewUser();*/
multiSol.form.onAddUserModalShow();
multiSol.MsgRouter.getProfiles();
multiSol.eventDelegation();