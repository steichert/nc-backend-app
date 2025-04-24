CREATE SCHEMA IF NOT EXISTS NcBackendApp;

CREATE TABLE Shedlock (
	Name        varchar(64)     PRIMARY KEY,
	LockUntil   timestamp(3)    NOT NULL,
	LockedAt    timestamp(3)    DEFAULT now() NOT NULL,
	LockedBy    varchar(255)    NOT NULL
);

CREATE TABLE Events (
	Id              int8            PRIMARY KEY AUTO_INCREMENT,
	Title           varchar(255)    NOT NULL,
	Description     text            NULL,
	State           varchar(255)    NOT NULL,
	EventDate       timestamp       NOT NULL,
	ActionLabel     varchar(255)    NULL,
	ActionUrl       varchar(255)    NULL,
	PublicId        int8            NOT NULL,
	CreationDate    timestamp(3)    NOT NULL,
	ModifiedDate    timestamp(3)    NULL
);

CREATE TABLE EventAttachments (
	Id                  int8            PRIMARY KEY AUTO_INCREMENT,
	AttachmentUrl       varchar(255)    NULL,
	AttachmentFilename  varchar(255)    NULL,
	AttachmentPublicId  varchar(255)    NULL,
	AttachmentType      varchar(50)     NOT NULL,
	EventDate           timestamp(3)    NOT NULL,
	EventId             int8            NOT NULL
);

ALTER TABLE EventAttachments ADD CONSTRAINT "fk_EventAttachments_Events" FOREIGN KEY (EventId) REFERENCES Events(Id);