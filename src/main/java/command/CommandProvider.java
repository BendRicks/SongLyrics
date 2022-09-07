package command;

import command.impl.abstraction.Command;
import command.impl.album.*;
import command.impl.main.ChangeLanguageCommand;
import command.impl.main.GetMainPageCommand;
import command.impl.performer.*;
import command.impl.track.*;
import command.impl.user.*;
import command.impl.user.admin.*;

import java.util.HashMap;
import java.util.Map;

public class CommandProvider {

    private static final Map<String, Command> postCommands = new HashMap<String, Command>() {{
        put("sign_up", new SignUpCommand());
        put("sign_in", new SignInCommand());
        put("change_username", new ChangeUsernameCommand());
        put("change_password", new ChangePasswordCommand());
        put("track_search", new SearchTracksCommand());
        put("album_search", new SearchAlbumsCommand());
        put("performer_search", new SearchPerformersCommand());
        put("user_search", new SearchUsersCommand());
        put("create_performer", new CreatePerformerCommand());
        put("create_album", new CreateAlbumCommand());
        put("create_track", new CreateTrackCommand());
        put("change_user_status", new ChangeUserStatusCommand());
        put("change_role", new ChangeUserRoleCommand());
        put("change_track", new ChangeTrackCommand());
        put("change_album", new ChangeAlbumCommand());
        put("change_performer", new ChangePerformerCommand());
        put("change_track_status", new ChangeTrackStatusCommand());
        put("change_album_status", new ChangeAlbumStatusCommand());
        put("change_performer_status", new ChangePerformerStatusCommand());
    }};

    private static final Map<String, Command> getCommands = new HashMap<String, Command>() {{
        put("main_page", new GetMainPageCommand());
        put("sign_up", new GetSignUpPageCommand());
        put("sign_in", new GetSignInPageCommand());
        put("log_out", new LogOutCommand());
        put("change_lang", new ChangeLanguageCommand());
        put("user_page", new GetUserPageCommand());
        put("get_performer", new GetPerformerPage());
        put("get_album", new GetAlbumPage());
        put("get_track", new GetTrackPage());
        put("track_search", new GetTrackSearchPageCommand());
        put("album_search", new GetAlbumSearchPageCommand());
        put("performer_search", new GetPerformerSearchPageCommand());
        put("user_search", new GetUserSearchPageCommand());
        put("turn_performer_search_page", new TurnPerformersPageCommand());
        put("turn_album_search_page", new TurnAlbumsPageCommand());
        put("turn_track_search_page", new TurnTracksPageCommand());
        put("turn_user_search_page", new TurnUsersPageCommand());
        put("create_performer", new GetPerformerCreatePageCommand());
        put("create_album", new GetAlbumCreatePageCommand());
        put("create_track", new GetTrackCreatePageCommand());
        put("change_track", new GetChangeTrackPageCommand());
        put("change_album", new GetChangeAlbumPageCommand());
        put("change_performer", new GetChangePerformerPageCommand());
    }};

    public static Command findPostCommand(String commandName) {
        return postCommands.get(commandName);
    }

    public static Command findGetCommand(String commandName) {
        return getCommands.get(commandName);
    }

}
