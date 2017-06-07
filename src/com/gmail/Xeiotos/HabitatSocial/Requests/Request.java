package com.gmail.Xeiotos.HabitatSocial.Requests;

/**
 *
 * @author Xeiotos
 */
public abstract class Request {

    protected final String requestedPlayer;
    protected final String requestingPlayer;
    protected boolean accepted;

    public Request(String requestedPlayer, String requestingPlayer) {
        this.requestedPlayer = requestedPlayer;
        this.requestingPlayer = requestingPlayer;
    }

    /**
     * Get the player requesting
     *
     * @return Player requesting
     */
    public String getRequestingPlayer() {
        return requestingPlayer;
    }
    
    /**
     * Get the player being requested
     *
     * @return Player being requested
     */
    public String getRequestedPlayer() {
        return requestedPlayer;
    }
    
    public abstract void accept();
    
    public abstract void decline();
    
    /**
     * Get if the request is accepted
     *
     * @return True if accepted, false if not
     */
    public boolean isAccepted() {
        return accepted;
    }
}
