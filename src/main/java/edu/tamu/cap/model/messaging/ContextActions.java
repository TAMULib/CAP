package edu.tamu.cap.model.messaging;

import edu.tamu.weaver.messaging.model.MessageActions;

public enum ContextActions implements MessageActions {
    METADATA_CREATE, METADATA_UPDATE, METADATA_DELETE, RESOURCE_CREATE, RESOURCE_DELETE
}