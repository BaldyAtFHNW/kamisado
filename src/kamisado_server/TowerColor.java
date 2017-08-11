package kamisado_server;

/**
 * 
 * 	@author Simon Bieri
 * 	@date 2017-08-11
 *	Enum to make sure that the server always uses the correct strings to describe the server.
 *	As the client never generates any such strings without input from the server we did not include the enums in the client package.
 *
 *
 *	Please note: The GUI shapes have just recently been changed from white and black circles to black diamonds and black circles.
 *	We got feedback from a lot of people that they are better visible.
 *
 *	Sadly there was no time anymore to change the references in the code.
 *	So black player = diamond player
 *	white player = black circle
 *
 */
public enum TowerColor {
	BBROWN, BGREEN, BRED, BYELLOW, BPINK, BPURPLE, BBLUE, BORANGE, 
	WBROWN, WGREEN, WRED, WYELLOW, WPINK, WPURPLE, WBLUE, WORANGE
}
