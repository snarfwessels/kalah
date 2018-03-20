package eu.snarf.kalah.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frans on 3/20/2018.
 */
public class Messages implements Serializable {
    private final List<String> errors;
    private final List<String> infos;

    public Messages(){
        errors = new ArrayList<>();
        infos = new ArrayList<>();
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public boolean hasInfo(){
        return !infos.isEmpty();
    }

    public void addErrorMessage(final String message) {
        errors.add(message);
    }

    public void addInfoMessage(final String message) {
        infos.add(message);
    }

    public List<String> getErrorMessages() {
        return errors;
    }

    public List<String> getInfoMessages() {
        return infos;
    }
}
