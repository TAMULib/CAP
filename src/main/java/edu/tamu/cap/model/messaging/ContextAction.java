package edu.tamu.cap.model.messaging;

import edu.tamu.weaver.messaging.model.MessageActions;

public enum ContextAction implements MessageActions {
    METADATA_CREATE, METADATA_UPDATE, METADATA_DELETE, RESOURCE_CREATE, RESOURCE_DELETE
}