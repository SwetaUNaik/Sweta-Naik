
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;

class Sprite {

	/*
	 *	
	 *	Sprite is the basic object that is drawn onto the screen. The dots 
	 *	are all Sprite objects. ConnectionSprite and BoxSprite are subclasses
	 *	of Sprite. Sprite has a method check to see if a point is within
	 *	the drawn object. Sprite also has method to draw the Sprite to the screen.	 
	 *
	 */

    Polygon shape;	//	The shape that is to be drawn
    Color color;	//	The color of the shape
    int width;		//	Width of the Sprite
    int height;		//	Height of the Sprite
    int x;			//	Horizontal coordinate of the center of the sprite
    int y;			//	Vertical coordinate of the center of the sprite
    
    public Sprite() {
    	//	Initialize all the fields
        shape=new Polygon();
        width=0;
        height=0;
        x=0;
        y=0;
        color=Color.BLACK;
    }
    
    public void render(Graphics g) {
    	//	The render method is responsible for positioning the sprite at the proper location
    	
        g.setColor(color);
        
        Polygon renderedShape=new Polygon();
        for(int i=0; i<shape.npoints; i++) {
            int renderedx=shape.xpoints[i] + x + width / 2;
            int renderedy=shape.ypoints[i] + y + height / 2;
            renderedShape.addPoint(renderedx, renderedy);
        }
        g.fillPolygon(renderedShape);
    }
    
    public boolean containsPoint(int x, int y) {
    	//	This returns true only if the point (x, y) is contained within the visible shape of the sprite
    	
    	return shape.contains(x - this.x - width /2, y - this.y - height /2);
    }
}

class ConnectionSprite extends Sprite {

	/*
	 *
	 *	ConnectionSprite is a sublcass of Sprite. There are two types of connections: vertical
	 *	connections between dots and horizontal connections between sprites. The static method
	 *	createConnection is a convenience method to create the ConnectionSprite at the proper
	 *	coordinates and build its shape.
	 *
	 */

    public static final int HORZ_CONN=1;
    public static final int VERT_CONN=2;
    
    boolean connectionMade;	// Tracks wether the ConnectionSprite has been clicked on
    
    public ConnectionSprite() {
    	// Initialize all the fields
        super();
        
        connectionMade=false;
        color=Color.WHITE;
    }
    
    public static ConnectionSprite createConnection(int type, int x, int y) {
    	ConnectionSprite conn=new ConnectionSprite();
    	
        if(type==ConnectionSprite.HORZ_CONN) {
        	conn.width=Dots.DOT_GAP;
        	conn.height=Dots.DOT_SIZE;
        } else if(type==ConnectionSprite.VERT_CONN) {
        	conn.width=Dots.DOT_SIZE;
        	conn.height=Dots.DOT_GAP;
        } else {
        	return null;
        }
        
        conn.x=x;
        conn.y=y;
        
        conn.shape.addPoint(-conn.width/2, -conn.height/2);
        conn.shape.addPoint(-conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, conn.height/2);
        conn.shape.addPoint(conn.width/2, -conn.height/2);
        
        return conn;
    }
}

class BoxSprite extends Sprite {

	/*
	 *
	 *	BoxSprite is a subclass of Sprite. BoxSprites represent the actual boxes made up by the Dot 
	 *	Sprites and ConnectionSprites. BoxSprite contains references to the four ConnectionSprites
	 *	which make up its borders. The isBoxed method returns true when all four of the border
	 *	ConnectionSprites have true connectionMade fields. BoxSprites should be created using the
	 *	static createBox method.
	 *
	 */

	ConnectionSprite[] horizontalConnections;	//	The ConnectionSprites that are the top and bottom borders of the box
	ConnectionSprite[] verticalConnections;		//	The ConnectionSprites that are the left and right borders of the box

	int player;	//	Tracks the player that closed the box

	public BoxSprite() {
		super();

		color=Color.WHITE;	//	Initially the box should be the same color as the background

		horizontalConnections=new ConnectionSprite[2];
		verticalConnections=new ConnectionSprite[2];

		width=Dots.DOT_GAP;
		height=Dots.DOT_GAP;

		shape.addPoint(-width/2, -height/2);
        shape.addPoint(-width/2, height/2);
        shape.addPoint(width/2, height/2);
        shape.addPoint(width/2, -height/2);
	}	

	public boolean isBoxed() {
		boolean boxed=true;

		for(int i=0; i<2; i++) {
			if(!horizontalConnections[i].connectionMade || !verticalConnections[i].connectionMade) {
				boxed=false;
			}
		}

		return boxed;
	}

	public static BoxSprite createBox(int x, int y, ConnectionSprite[] horizontalConnections, ConnectionSprite[] verticalConnections) {
		BoxSprite box=new BoxSprite();
		box.player=0;
		box.x=x;
		box.y=y;
		box.horizontalConnections=horizontalConnections;
		box.verticalConnections=verticalConnections;
		return box;
	}
}


