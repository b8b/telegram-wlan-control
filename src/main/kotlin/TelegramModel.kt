import com.fasterxml.jackson.annotation.JsonProperty

data class User(
        @JsonProperty("id") val id: Int,
        @JsonProperty("first_name") val firstName: String? = null,
        @JsonProperty("last_name") val lastName: String? = null,
        @JsonProperty("username") val username: String? = null,
        @JsonProperty("is_bot") val bot: Boolean = false,
        @JsonProperty("language_code") val languageCode: String? = null
)

data class Chat(
        @JsonProperty("id") val id: Int,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("username") val username: String? = null,
        @JsonProperty("first_name") val firstName: String? = null,
        @JsonProperty("last_name") val lastName: String? = null,
        @JsonProperty("all_members_are_administrators") val allAdmins: Boolean? = null
)

data class Audio(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("duration") val duration: Int? = null,
        @JsonProperty("performer") val performer: String? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("mime_type") val mimeType: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class PhotoSize(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("width") val width: Int? = null,
        @JsonProperty("height") val height: Int? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Document(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("thumb") val thumb: PhotoSize? = null,
        @JsonProperty("file_name") val fileName: String? = null,
        @JsonProperty("mime_type") val mimeType: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Animation(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("thumb") val thumb: PhotoSize? = null,
        @JsonProperty("file_name") val fileName: String? = null,
        @JsonProperty("mime_type") val mimeType: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Game(
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("photo") val photo: List<PhotoSize>? = null,
        @JsonProperty("text") val text: String? = null,
        @JsonProperty("text_entities") val textEntities: List<MessageEntity>? = null,
        @JsonProperty("animation") val animation: Animation? = null
)

data class Sticker(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("width") val width: Int? = null,
        @JsonProperty("height") val height: Int? = null,
        @JsonProperty("thumb") val thumb: PhotoSize? = null,
        @JsonProperty("emoji") val emoji: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Video(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("width") val width: Int? = null,
        @JsonProperty("height") val height: Int? = null,
        @JsonProperty("duration") val duration: Int? = null,
        @JsonProperty("thumb") val thumb: PhotoSize? = null,
        @JsonProperty("mime_type") val mimeType: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class VideoNote(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("length") val length: Int? = null,
        @JsonProperty("duration") val duration: Int? = null,
        @JsonProperty("thumb") val thumb: PhotoSize? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Voice(
        @JsonProperty("file_id") val fileId: String? = null,
        @JsonProperty("duration") val duration: Int? = null,
        @JsonProperty("mime_type") val mimeType: String? = null,
        @JsonProperty("file_size") val fileSize: Int? = null
)

data class Contact(
        @JsonProperty("phone_number") val phoneNumber: String? = null,
        @JsonProperty("first_name") val firstName: String? = null,
        @JsonProperty("last_name") val lastName: String? = null,
        @JsonProperty("user_id") val userId: Int? = null
)

data class Location(
        @JsonProperty("longitude") val longitude: Double? = null,
        @JsonProperty("latitude") val latitude: Double? = null
)

data class Venue(
        @JsonProperty("location") val location: Location? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("address") val address: String? = null,
        @JsonProperty("foursquare_id") val foursquareId: String? = null
)

data class Invoice(
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("start_parameter") val startParameter: String? = null,
        @JsonProperty("currency") val currency: String? = null,
        @JsonProperty("total_amount") val totalAmount: Int? = null
)

data class ShippingAddress(
        @JsonProperty("country_code") val countryCode: String? = null,
        @JsonProperty("state") val state: String? = null,
        @JsonProperty("city") val city: String? = null,
        @JsonProperty("street_line1") val streetLine1: String? = null,
        @JsonProperty("street_line2") val streetLine2: String? = null,
        @JsonProperty("post_code") val postCode: String? = null
)

data class OrderInfo(
        @JsonProperty("name") val name: String? = null,
        @JsonProperty("phone_number") val phoneNumber: String? = null,
        @JsonProperty("email") val email: String? = null,
        @JsonProperty("shipping_address") val shippingAddress: ShippingAddress? = null
)

data class SuccessfulPayment(
        @JsonProperty("currency") val currency: String? = null,
        @JsonProperty("total_amount") val totalAmount: Int? = null,
        @JsonProperty("invoice_payload") val invoicePayload: String? = null,
        @JsonProperty("shipping_option_id") val shippingOptionId: String? = null,
        @JsonProperty("order_info") val orderInfo: OrderInfo? = null,
        @JsonProperty("telegram_payment_charge_id") val telegramPaymentChargeId: String? = null,
        @JsonProperty("provider_payment_charge_id") val providerPaymentChargeId: String? = null
)

data class MessageEntity(
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("offset") val offset: Int? = null,
        @JsonProperty("length") val length: Int? = null,
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("user") val user: User? = null
)

data class Message(
        @JsonProperty("message_id") val messageId: Int,
        @JsonProperty("from") val from: User? = null,
        @JsonProperty("date") val date: Int? = null,
        @JsonProperty("chat") val chat: Chat? = null,
        @JsonProperty("forward_from") val forwardFrom: User? = null,
        @JsonProperty("forward_from_chat") val forwardFromChat: Chat? = null,
        @JsonProperty("forward_from_message_id") val forwardFromMessageId: Int? = null,
        @JsonProperty("forward_date") val forwardDate: Int? = null,
        @JsonProperty("reply_to_message") val replyToMessage: Message? = null,
        @JsonProperty("edit_date") val editDate: Int? = null,
        @JsonProperty("text") val text: String? = null,
        @JsonProperty("entities") val entities: List<MessageEntity>? = null,
        @JsonProperty("audio") val audio: Audio? = null,
        @JsonProperty("document") val document: Document? = null,
        @JsonProperty("game") val game: Game? = null,
        @JsonProperty("photo") val photo: List<PhotoSize>? = null,
        @JsonProperty("sticker") val sticker: Sticker? = null,
        @JsonProperty("video") val video: Video? = null,
        @JsonProperty("video_note") val videoNote: VideoNote? = null,
        @JsonProperty("voice") val voice: Voice? = null,
        @JsonProperty("caption") val caption: String? = null,
        @JsonProperty("contact") val contact: Contact? = null,
        @JsonProperty("location") val location: Location? = null,
        @JsonProperty("venue") val venue: Venue? = null,
        @JsonProperty("new_chat_members") val newChatMembers: List<User>? = null,
        @JsonProperty("left_chat_member") val leftChatMember: User? = null,
        @JsonProperty("new_chat_title") val newChatTitle: String? = null,
        @JsonProperty("new_chat_photo") val newChatPhoto: List<PhotoSize>? = null,
        @JsonProperty("delete_chat_photo") val deleteChatPhoto: Boolean? = null,
        @JsonProperty("group_chat_created") val groupChatCreated: Boolean? = null,
        @JsonProperty("supergroup_chat_created") val supergroupChatCreated: Boolean? = null,
        @JsonProperty("channel_chat_created") val channelChatCreated: Boolean? = null,
        @JsonProperty("migrate_to_chat_id") val migrateToChatId: Int? = null,
        @JsonProperty("migrate_from_chat_id") val migrateFromChatId: Int? = null,
        @JsonProperty("pinned_message") val pinnedMessage: Message? = null,
        @JsonProperty("invoice") val invoice: Invoice? = null,
        @JsonProperty("successful_payment") val successfulPayment: SuccessfulPayment? = null
)

class InlineQuery
class ChosenInlineResult

class CallbackQuery
class ShippingQuery
class PreCheckoutQuery

data class Update(
        @JsonProperty("update_id") val updateId: Int,
        @JsonProperty("message") val message: Message?,
        @JsonProperty("edited_message") val editedMessage: Message?,
        @JsonProperty("channel_post") val channelPost: Message?,
        @JsonProperty("edited_channel_post") val editedChannelPost: Message?,
        @JsonProperty("inline_query") val inlineQuery: InlineQuery?,
        @JsonProperty("chosen_inline_result") val chosenInlineResult: ChosenInlineResult?,
        @JsonProperty("callback_query") val callbackQuery: CallbackQuery?,
        @JsonProperty("shipping_query") val shippingQuery: ShippingQuery?,
        @JsonProperty("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery?
)

data class ChatMember(
        val user: User,
        val status: String,
        @JsonProperty("can_be_edited") val canBeEdited: Boolean,
        @JsonProperty("can_change_info") val canChangeInfo: Boolean,
        @JsonProperty("can_delete_messages") val canDeleteMessages: Boolean,
        @JsonProperty("can_invite_users") val canInviteUsers: Boolean,
        @JsonProperty("can_restrict_members") val canRestrictMembers: Boolean,
        @JsonProperty("can_promote_members") val canPromoteMembers: Boolean
)
