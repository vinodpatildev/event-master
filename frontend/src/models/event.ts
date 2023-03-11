export interface Event {
    _id: string,
    title: string,
    department: string,
    organizer:string,
    description:string,
    type: string,
    event_link: string,
    start: Date,
    end: Date,
    createdAt: string,
    updatedAt: string
}